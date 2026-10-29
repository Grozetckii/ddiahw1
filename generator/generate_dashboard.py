import os
import requests

GRAFANA_URL = os.environ["GRAFANA_URL"]
GRAFANA_USER = os.environ["GRAFANA_USER"]
GRAFANA_PASSWORD = os.environ["GRAFANA_PASSWORD"]
APP_URL = os.environ["APP_URL"]
DATASOURCE_UID = "prometheus-ds"

response = requests.get(f"{APP_URL}/handlers")
response.raise_for_status()
handlers = response.json()

panels = []
y_position = 0

for handler in handlers:
    method, path = handler.split(" ", 1)
    filter_expr = f'uri="{path}", method="{method}", datacenter=~"$datacenter", pod_id=~"$pod_id"'

    rps_panel = {
        "type": "timeseries",
        "title": "RPS",
        "datasource": {"type": "prometheus", "uid": DATASOURCE_UID},
        "gridPos": {"h": 8, "w": 8, "x": 0, "y": 0},
        "targets": [{
            "expr": f'sum(rate(http_server_requests_seconds_count{{{filter_expr}}}[5m]))',
            "legendFormat": "RPS"
        }]
    }

    latency_panel = {
        "type": "timeseries",
        "title": "Latency",
        "datasource": {"type": "prometheus", "uid": DATASOURCE_UID},
        "gridPos": {"h": 8, "w": 8, "x": 8, "y": 0},
        "targets": [
            {"expr": f'histogram_quantile(0.50, sum by (le) (rate(http_server_requests_seconds_bucket{{{filter_expr}}}[5m])))', "legendFormat": "p50"},
            {"expr": f'histogram_quantile(0.95, sum by (le) (rate(http_server_requests_seconds_bucket{{{filter_expr}}}[5m])))', "legendFormat": "p95"},
            {"expr": f'histogram_quantile(0.99, sum by (le) (rate(http_server_requests_seconds_bucket{{{filter_expr}}}[5m])))', "legendFormat": "p99"}
        ]
    }

    errors_panel = {
        "type": "timeseries",
        "title": "Errors",
        "datasource": {"type": "prometheus", "uid": DATASOURCE_UID},
        "gridPos": {"h": 8, "w": 8, "x": 16, "y": 0},
        "targets": [{
            "expr": f'sum by (status) (rate(http_server_requests_seconds_count{{{filter_expr}, status=~"4..|5.."}}[5m]))',
            "legendFormat": "{{status}}"
        }]
    }

    row_panel = {
        "type": "row",
        "title": f"{method} {path}",
        "collapsed": True,
        "gridPos": {"h": 1, "w": 24, "x": 0, "y": y_position},
        "panels": [rps_panel, latency_panel, errors_panel]
    }

    panels.append(row_panel)
    y_position += 1

dashboard_payload = {
    "dashboard": {
        "title": "Auto-generated Dashboard DDIA-HW-1",
        "editable": False,
        "time": {
            "from": "now-15m",
            "to": "now"
        },
        "refresh": "5s",
        "panels": panels,
        "templating": {
            "list": [
                {
                    "name": "datacenter",
                    "type": "query",
                    "datasource": {"type": "prometheus", "uid": DATASOURCE_UID},
                    "query": "label_values(http_server_requests_seconds_count, datacenter)",
                    "includeAll": True,
                    "multi": True,
                    "refresh": 1,
                    "current": {"selected": True, "text": "All", "value": "$__all"}
                },
                {
                    "name": "pod_id",
                    "type": "query",
                    "datasource": {"type": "prometheus", "uid": DATASOURCE_UID},
                    "query": "label_values(http_server_requests_seconds_count, pod_id)",
                    "includeAll": True,
                    "multi": True,
                    "refresh": 1,
                    "current": {"selected": True, "text": "All", "value": "$__all"}
                }
            ]
        }
    },
    "overwrite": True
}

auth = (GRAFANA_USER, GRAFANA_PASSWORD)
api_url = f"{GRAFANA_URL}/api/dashboards/db"
resp = requests.post(api_url, json=dashboard_payload, auth=auth)
resp.raise_for_status()
print(f"Dashboard created: {resp.json()['url']}")
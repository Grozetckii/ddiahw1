FROM python:3.11-slim
WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt
COPY generate_dashboard.py .
CMD ["python", "generate_dashboard.py"]
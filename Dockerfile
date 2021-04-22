# Docker inheritance
FROM repo.backbase.com/backbase-docker-releases/notifications-service:DBS-2.20.3

ARG JAR_FILE
COPY target/${JAR_FILE} /app/WEB-INF/lib
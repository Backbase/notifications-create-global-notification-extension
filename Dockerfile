# Docker inheritance
FROM repo.backbase.com/backbase-docker-releases/notifications-service:DBS-2.19.0

ARG JAR_FILE
COPY target/${JAR_FILE} /app/WEB-INF/lib
# Docker inheritance
FROM repo.backbase.com/backbase-docker-releases/notifications-service:DBS-2.18.1

ARG JAR_FILE
COPY target/${JAR_FILE} /app/WEB-INF/lib
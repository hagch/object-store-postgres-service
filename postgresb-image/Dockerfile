FROM postgres:14.2
ADD postgres-json-schema /postgres-json-schema
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y build-essential postgresql-server-dev-all
RUN cd /postgres-json-schema && make install && make installcheck
COPY install-json.sh /docker-entrypoint-initdb.d/

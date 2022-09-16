FROM registry.access.redhat.com/ubi8/openjdk-11-runtime

ENV LANG en_US.UTF-8
USER root

RUN microdnf update -y && \
    microdnf install -y --nodocs zip && microdnf clean all && rm -rf /var/cache/yum/*

RUN curl -sL https://github.com/GoogleContainerTools/jib/releases/download/v0.11.0-cli/jib-jre-0.11.0.zip -o jib.zip && \
    unzip jib.zip && rm -rf jib.zip

ENV PATH="${PATH}:${HOME}/jib-0.11.0/bin"

# Restore the original user
USER jboss

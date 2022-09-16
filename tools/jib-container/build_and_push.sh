#! /bin/bash
set -euxo pipefail

SCRIPT_DIR=$( cd -- "$( dirname -- "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )

docker build ${SCRIPT_DIR} -t docker.io/andreatp/jib:latest
docker push docker.io/andreatp/jib:latest

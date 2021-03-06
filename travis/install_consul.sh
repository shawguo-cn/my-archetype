#!/usr/bin/env bash
#https://github.com/spring-cloud/spring-cloud-consul/blob/master/src/main/bash/travis_install_consul.sh

CONSUL_VER="0.6.3"
CONSUL_ZIP="consul_${CONSUL_VER}_linux_amd64.zip"
IGNORE_CERTS="${IGNORE_CERTS:-no}"

# cleanup
rm "consul_*"
rm "consul"
# install consul
if [[ "${IGNORE_CERTS}" == "no" ]] ; then
  echo "Downloading consul with certs verification"
  wget "https://releases.hashicorp.com/consul/${CONSUL_VER}/${CONSUL_ZIP}"
else
  echo "WARNING... Downloading consul WITHOUT certs verification"
  wget "https://releases.hashicorp.com/consul/${CONSUL_VER}/${CONSUL_ZIP}" --no-check-certificate
fi
unzip ${CONSUL_ZIP}
# check
./consul --version
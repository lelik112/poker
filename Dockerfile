FROM ubuntu:latest
RUN apt-get update && apt-get install git -y
RUN git clone https://github.com/lelik112/poker.git
WORKDIR ./poker
RUN git checkout develop
RUN chmod +x ./prepare.sh
RUN ./prepare.sh
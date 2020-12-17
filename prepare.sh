TZ=Europe/Minsk
SCALA_VERSION=2.13.1
SBT_VERSION=1.2.8
JAVA_HOME_11_X64=/usr/lib/jvm/java-11-openjdk-amd64

ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

apt-get update \
 && apt-get install -y software-properties-common

apt-get update \
 && apt-get install -y --no-install-recommends openjdk-11-jdk \
 && rm -rf /var/lib/apt/lists/*

apt-get update \
  && apt-get install curl -y

curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
  dpkg -i sbt-$SBT_VERSION.deb && \
  rm sbt-$SBT_VERSION.deb && \
  apt-get install sbt -y

sbt
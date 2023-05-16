# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.

# Use a maven base image
FROM maven:3.8.3-openjdk-17-slim

# Set the working directory
WORKDIR /app

# Copy the pom.xml file to the container
COPY pom.xml ./
COPY src/main/demo/pom.xml ./spring-boot/

# Copy the source code to the container
COPY src ./src
COPY src/main/demo/src ./spring-boot/src/

# Build the project and create the JAR file
RUN mvn clean install
RUN mvn -f ./spring-boot/pom.xml clean package

# expose the service on the port 8080
EXPOSE 8080

# Run the container right after the build
CMD ["java" , "-jar", "./spring-boot/target/demo-0.0.1-SNAPSHOT.jar"]
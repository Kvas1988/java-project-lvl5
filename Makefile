install:
	./gradlew clean install

run-dist:
	./build/install/app/bin/app

test:
	./gradlew test

build:
	./gradlew build

.PHONY: build

check-updates:
	./gradlew dependencyUpdates

migrations:
	./gradlew diffChangeLog

act:
	act --secret-file my.secrets
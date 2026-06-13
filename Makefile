.PHONY: install dev apk clean test check-device

PACKAGE  := com.lykimq_uyen.french_nationality
ACTIVITY := $(PACKAGE)/.MainActivity
GRADLE   := ./gradlew

# Prefer physical phone over emulator; override with ANDROID_SERIAL=<id>
ADB_DEVICE := $(if $(ANDROID_SERIAL),$(ANDROID_SERIAL),$(shell adb devices 2>/dev/null | awk '/device$$/ && !/^emulator-/ {print $$1; exit}'))
ifeq ($(ADB_DEVICE),)
  ADB_DEVICE := $(shell adb devices 2>/dev/null | awk '/device$$/ {print $$1; exit}')
endif
ADB := adb -s $(ADB_DEVICE)

# Source paths watched by make dev
WATCH_PATHS := app/src/main/java app/src/main/res app/src/main/AndroidManifest.xml app/build.gradle.kts

check-device:
	@adb devices | grep -q 'device$$' || (echo "No Android device found."; exit 1)
	@test -n "$(ADB_DEVICE)" || (echo "Could not resolve a target device."; exit 1)

install: check-device
	ANDROID_SERIAL=$(ADB_DEVICE) $(GRADLE) installDebug

dev: check-device
	@command -v inotifywait >/dev/null || (echo "Install inotify-tools: sudo apt install inotify-tools"; exit 1)
	@echo "Device: $(ADB_DEVICE)"
	ANDROID_SERIAL=$(ADB_DEVICE) $(GRADLE) installDebug
	$(ADB) shell am start -n $(ACTIVITY)
	@echo "Watching for changes... (Ctrl+C to stop)"
	@while inotifywait -r -e close_write,create,delete,move $(WATCH_PATHS) 2>/dev/null; do \
		echo "--- Change detected, deploying ---"; \
		ANDROID_SERIAL=$(ADB_DEVICE) $(GRADLE) installDebug -q && \
		$(ADB) shell am force-stop $(PACKAGE) && \
		$(ADB) shell am start -n $(ACTIVITY) && \
		echo "--- Done ---"; \
	done

apk:
	$(GRADLE) assembleDebug
	@echo "APK: app/build/outputs/apk/debug/app-debug.apk"

clean:
	$(GRADLE) clean

test:
	$(GRADLE) test

@echo off
echo Creating Service Registry...
cf create-service p-service-registry standard service-registry > nul
:check
  cf service service-registry | find "succeeded" > nul
  if errorlevel 1 goto :check
  echo Service Registry created. Pushing applications.
  pushd homeowner
  cf push -p build/libs/homeowner-0.0.1-SNAPSHOT.jar
  popd
  pushd visitor
  cf push -p build/libs/visitor-0.0.1-SNAPSHOT.jar
  popd
  echo Done!

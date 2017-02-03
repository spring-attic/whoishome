echo -n "Creating Service Registry..."
{
  cf create-service p-service-registry standard service-registry
} &> /dev/null
until [ `cf service service-registry | grep -c "succeeded"` -eq 1  ]
do
  echo -n "."
done
echo
echo "Service Registry created. Pushing applications."
pushd homeowner && cf push -p build/libs/homeowner-0.0.1-SNAPSHOT.jar
popd
pushd visitor && cf push -p build/libs/visitor-0.0.1-SNAPSHOT.jar
popd
echo "" && echo "Done!" && echo ""

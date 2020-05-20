kubectl apply -f - <<EOF                              
apiVersion: v1
kind: ServiceAccount
metadata:
  name: build-robot
EOF

kubectl create rolebinding cluster-admin-build-robot \
  --clusterrole=cluster-admin \
  --serviceaccount=dev-test:build-robot \
  --namespace=dev-test
  
  
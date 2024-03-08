#!/usr/bin/env bash

# reference: https://kubernetes.io/zh-cn/docs/tasks/manage-kubernetes-objects/kustomization/
dir=configmap_env
# kubectl kustomize ./$dir >  $dir/output.yaml

dir=configmap_properties
kubectl kustomize ./$dir >  $dir/output.yaml

# configMapGenerator
dir=deployment_has_configmap
kubectl kustomize ./$dir > $dir/output.yaml

# secretGenerator
dir=deployment_has_secret
kubectl kustomize ./$dir > $dir/output.yaml

# generatorOptions
# Setting cross-cutting fields 设置贯穿字段
dir=set1_cross_cutting_fields
kubectl kustomize ./$dir > $dir/output.yaml

# Composing and Customizing Resources 组合和自定义资源
dir=set2_composing_resources
kubectl kustomize ./$dir > $dir/output.yaml

dir=set3_customizing_resources_by_patchesStrategicMerge
kubectl kustomize ./$dir > $dir/output.yaml

dir=set4_customizing_resources_by_patchesJson6902
kubectl kustomize ./$dir > $dir/output.yaml

dir=set4_customizing_resources_by_patchesJson6902_env
kubectl kustomize ./$dir > $dir/output.yaml


# Bases and Overlays 基础和叠加 (maybe kubectl版本问题)
dir=set5_overlays_and_base
tmp=$dir/prod
# kubectl kustomize ./$tmp > $tmp/output.yaml

tmp=$dir/dev
# kubectl kustomize ./$tmp > $tmp/output.yaml


# How to apply/view/delete objects using Kustomize
dir=set6_apply
# kubectl apply -k ./$dir > $dir/output.yaml
kubectl get -k ./
kubectl describe -k ./
kubectl delete -k ./



# BinK8s

 - **B**uild
 - **in**
 - **K**ubernetes

This is project to ease and automate the building of container images in Kubernetes using an operator.

This blog shows multiple ways to build images in Docker: https://snyk.io/blog/building-docker-images-kubernetes/

We are going here to explore how to wrap the building of images in kubernetes such that it can be easily automated.

 GOALS:

 - Pick up a container image definition and push a container image to a registry
 - Make the process easy to monitor/debug
 - Create meaningful Kubernetes resources with the output (e.g. the full image reference at the end)
 - Dead simple setup process with minimal requirements and meaningful defaults (e.g. no mandatory target image name and push to ttl.sh)
 - Full customizable for advanced use-cases

NON GOALS:

 - Full integration with one specific build tool

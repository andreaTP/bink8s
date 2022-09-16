package io.bink8s.controller;

public class Constants {

    private Constants() {}

    public final static String CONTAINER_IMAGE_NAME = "image-name";

    public final static String DOCKERFILE_NAME = "Dockerfile";
    public final static String DOCKERFILE_VOLUME_NAME = "dockerfile-volume";

    public final static String JOB_WORKDIR = "/mnt/workdir";

    public final static String DOCKERFILE_PATH = JOB_WORKDIR + "/" + DOCKERFILE_NAME;

    public final static String ENV_NAME_TARGET_IMAGE = "IMAGE_BUILD_TARGET_NAME";
    public final static String ENV_NAME_REGISTRY_CREDENTIALS = "IMAGE_BUILD_REGISTRY_CREDENTIALS";

    public final static String CREATE_SECRET_DEPENDENT_NAME = "create-dockerfile-secret";
    public final static String CREATE_JOB_DEPENDENT_NAME = "create-job";
}

package io.bink8s.controller;

import io.bink8s.v1.ImageBuild;
import io.fabric8.kubernetes.client.utils.KubernetesResourceUtil;

public class ImageName {

    private static final String TTL_SH = "ttl.sh/";

    private ImageName() {
    }

    private static String fallbackId(ImageBuild imageBuild) {
        // TODO: make it possible to have more "readable" names still avoiding conflicts
        String name = imageBuild.getMetadata().getName();
        String uid = imageBuild.getMetadata().getUid();
        String version = imageBuild.getMetadata().getResourceVersion();

        return TTL_SH + name + "/" + KubernetesResourceUtil.sanitizeName(uid + "-" + version) + ":1d";
    }

    public static String get(ImageBuild imageBuild) {
        String imageBuildTargetName = fallbackId(imageBuild);
        if (imageBuild.getSpec() != null &&
            imageBuild.getSpec().getTargetName() != null &&
            !imageBuild.getSpec().getTargetName().isEmpty()) {
            imageBuildTargetName = imageBuild.getSpec().getTargetName();
        }

        return imageBuildTargetName;
    }
}

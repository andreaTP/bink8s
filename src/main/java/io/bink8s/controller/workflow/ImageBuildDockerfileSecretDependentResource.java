package io.bink8s.controller.workflow;

import io.bink8s.controller.Constants;
import io.bink8s.v1.ImageBuild;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.fabric8.kubernetes.client.utils.KubernetesResourceUtil;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;

import java.util.Map;

public class ImageBuildDockerfileSecretDependentResource extends CRUDKubernetesDependentResource<Secret, ImageBuild> {

    public ImageBuildDockerfileSecretDependentResource() {
        super(Secret.class);
    }

    public ImageBuildDockerfileSecretDependentResource(Class<Secret> resourceType) {
        super(resourceType);
    }

    @Override
    protected Secret desired(ImageBuild imageBuild, Context<ImageBuild> context) {
        String dockerfileContent = imageBuild.getSpec().getDefinition(); // TODO load it from a user Secret? Copy vs. re-use?
        // Probably better to always copy to a "well-known" Secret
        // so that we can support ConfigMap and more in the future!

        return new SecretBuilder()
                .withNewMetadata()
                    .withName(imageBuild.getMetadata().getName())
                .endMetadata()
                .withStringData(Map.of(Constants.DOCKERFILE_NAME, dockerfileContent))
                .build();
    }
}

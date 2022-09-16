package io.bink8s.controller.workflow;

import io.bink8s.controller.Constants;
import io.bink8s.controller.ImageName;
import io.bink8s.v1.ImageBuild;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.api.model.SecretBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;

import java.util.Map;

public class ImageBuildOutputSecretDependentResource extends CRUDKubernetesDependentResource<Secret, ImageBuild> {

    public ImageBuildOutputSecretDependentResource() {
        super(Secret.class);
    }

    public ImageBuildOutputSecretDependentResource(Class<Secret> resourceType) {
        super(resourceType);
    }

    @Override
    protected Secret desired(ImageBuild imageBuild, Context<ImageBuild> context) {
        return new SecretBuilder()
                .withNewMetadata()
                    .withName(imageBuild.getMetadata().getName() + "-result") // TODO: sanitize this name?
                .endMetadata()
                .withStringData(Map.of(Constants.CONTAINER_IMAGE_NAME, ImageName.get(imageBuild)))
                .build();
    }
}

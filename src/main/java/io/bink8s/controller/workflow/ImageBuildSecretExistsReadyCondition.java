package io.bink8s.controller.workflow;

import io.bink8s.v1.ImageBuild;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.dependent.DependentResource;
import io.javaoperatorsdk.operator.processing.dependent.workflow.Condition;
import io.quarkus.logging.Log;

public class ImageBuildSecretExistsReadyCondition implements Condition<Secret, ImageBuild> {
    @Override
    public boolean isMet(
            DependentResource<Secret, ImageBuild> dependentResource,
            ImageBuild imageBuild, Context<ImageBuild> context) {

        var imageBuildSecret = dependentResource.getSecondaryResource(imageBuild).orElseThrow();
        Log.info("Checking condition on Secret : " + imageBuildSecret.getMetadata().getName()  + "\n" + Serialization.asYaml(imageBuildSecret));

        return imageBuildSecret != null;
    }
}

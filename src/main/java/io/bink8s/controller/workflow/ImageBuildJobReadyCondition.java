package io.bink8s.controller.workflow;

import io.bink8s.v1.ImageBuild;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.dependent.DependentResource;
import io.javaoperatorsdk.operator.processing.dependent.workflow.Condition;
import io.quarkus.logging.Log;

public class ImageBuildJobReadyCondition implements Condition<Job, ImageBuild> {
    @Override
    public boolean isMet(
            DependentResource<Job, ImageBuild> dependentResource,
            ImageBuild imageBuild, Context<ImageBuild> context) {

        var imageBuildJob = dependentResource.getSecondaryResource(imageBuild).orElseThrow();

        boolean ready = (imageBuildJob != null &&
                imageBuildJob.getStatus() != null &&
                (imageBuildJob.getStatus().getActive() == null || imageBuildJob.getStatus().getActive() == 0) &&
                imageBuildJob.getStatus().getSucceeded() != null &&
                imageBuildJob.getStatus().getSucceeded() > 0);

        Log.info("Checking condition on Job: " + imageBuildJob.getMetadata().getName()  + " - " + ready + "\n" + Serialization.asYaml(imageBuildJob));

        return ready;
    }
}

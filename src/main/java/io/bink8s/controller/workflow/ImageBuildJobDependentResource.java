package io.bink8s.controller.workflow;

import io.bink8s.controller.Constants;
import io.bink8s.controller.ImageName;
import io.bink8s.v1.ImageBuild;
import io.fabric8.kubernetes.api.model.EnvVar;
import io.fabric8.kubernetes.api.model.EnvVarBuilder;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder;
import io.fabric8.kubernetes.client.utils.Serialization;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.processing.dependent.kubernetes.CRUDKubernetesDependentResource;
import io.javaoperatorsdk.operator.processing.event.ResourceID;
import io.quarkus.logging.Log;

import java.util.ArrayList;
import java.util.List;

public class ImageBuildJobDependentResource extends CRUDKubernetesDependentResource<Job, ImageBuild> {

    public ImageBuildJobDependentResource() {
        super(Job.class); // TODO: check if it's correct
    }

    public ImageBuildJobDependentResource(Class<Job> resourceType) {
        super(resourceType);
    }

    @Override
    protected Job desired(ImageBuild imageBuild, Context<ImageBuild> context) {
        List<EnvVar> envVars = new ArrayList<>();

        envVars.add(new EnvVarBuilder()
                .withName(Constants.ENV_NAME_TARGET_IMAGE)
                .withValue(ImageName.get(imageBuild))
                .build());

        // TODO: Make sure that one can use `imagePullSecret` secrets for the credentials!
        // TODO: credentials are not yet integrated
        if (imageBuild.getSpec() != null &&
                imageBuild.getSpec().getRegistryCredentials() != null) {
            envVars.add(new EnvVarBuilder()
                    .withName(Constants.ENV_NAME_REGISTRY_CREDENTIALS)
                    .withValue(imageBuild.getSpec().getRegistryCredentials())
                    .build());
        }

        Job imageBuildJob = new JobBuilder()
                .withNewMetadata()
                    .withName(imageBuild.getMetadata().getName())
                .endMetadata()
                .withNewSpec()
                    .withNewTemplate() // TODO: make PodTemplate injectable
                        .withNewSpec()
                            .addNewVolume()
                                .withName(Constants.DOCKERFILE_VOLUME_NAME)
                                .withNewSecret()
                                    .withSecretName(imageBuild.getMetadata().getName())
                                .endSecret()
                            .endVolume()
                            .addNewContainer()
                                .withName("builder")
                                .withImage("gcr.io/kaniko-project/executor:latest") // TODO: make this configurable -> maybe just with podTemplate
                                // .withCommand("sh", "-c", "cat /mnt/workdir/Dockerfile") // DEBUG
                                .withArgs("--context=dir://" + Constants.JOB_WORKDIR + "",
                                          "--destination=" + ImageName.get(imageBuild) + "")
                                .withEnv(envVars)
                                .withWorkingDir(Constants.JOB_WORKDIR)
                                .addNewVolumeMount()
                                    .withName(Constants.DOCKERFILE_VOLUME_NAME)
                                    .withMountPath(Constants.DOCKERFILE_PATH)
                                    .withSubPath(Constants.DOCKERFILE_NAME)
                                .endVolumeMount()
                            .endContainer()
                            .withRestartPolicy("Never") // TODO: should it be customizable? Maybe with podTemplate!
                        .endSpec()
                    .endTemplate()
                .endSpec()
                .build();

        return imageBuildJob;
    }

    // WORKAROUND Job spec.template is unmodifiable
    // Update is basically forbidden
    @Override
    protected Job handleUpdate(Job actual, Job desired, ImageBuild primary, Context<ImageBuild> context) {
        return desired;
    }

    // WORKAROUND Job spec.template is unmodifiable
    // Update is basically forbidden
    @Override
    public Job update(Job actual, Job target, ImageBuild primary, Context<ImageBuild> context) {
        return target;
    }
}

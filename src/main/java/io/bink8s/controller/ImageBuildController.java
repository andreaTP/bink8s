package io.bink8s.controller;

import io.bink8s.controller.workflow.ImageBuildJobDependentResource;
import io.bink8s.controller.workflow.ImageBuildJobReadyCondition;
import io.bink8s.controller.workflow.ImageBuildDockerfileSecretDependentResource;
import io.bink8s.controller.workflow.ImageBuildOutputSecretDependentResource;
import io.bink8s.controller.workflow.ImageBuildSecretExistsReadyCondition;
import io.bink8s.v1.ImageBuild;
import io.bink8s.v1.ImageBuildStatusBuilder;
import io.javaoperatorsdk.operator.api.reconciler.Constants;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import io.javaoperatorsdk.operator.api.reconciler.dependent.Dependent;

@ControllerConfiguration(
        namespaces = Constants.WATCH_CURRENT_NAMESPACE,
        dependents = {
            @Dependent(
                    name = io.bink8s.controller.Constants.CREATE_SECRET_DEPENDENT_NAME,
                    type = ImageBuildDockerfileSecretDependentResource.class,
                    readyPostcondition = ImageBuildSecretExistsReadyCondition.class),
            @Dependent(
                    name = io.bink8s.controller.Constants.CREATE_JOB_DEPENDENT_NAME,
                    dependsOn = io.bink8s.controller.Constants.CREATE_SECRET_DEPENDENT_NAME,
                    type = ImageBuildJobDependentResource.class,
                    readyPostcondition = ImageBuildJobReadyCondition.class),
// NOT WORKING: Enabling it causes loops
// -> need a fix
//            @Dependent(
//                    dependsOn = io.bink8s.controller.Constants.CREATE_JOB_DEPENDENT_NAME,
//                    type = ImageBuildOutputSecretDependentResource.class,
//                    readyPostcondition = ImageBuildSecretExistsReadyCondition.class)
})
public class ImageBuildController implements Reconciler<ImageBuild> {

    @Override
    public UpdateControl<ImageBuild> reconcile(ImageBuild imageBuild, Context<ImageBuild> context) throws Exception {
        if (imageBuild.getStatus() == null) {
            imageBuild.setStatus(
                    new ImageBuildStatusBuilder()
                            .withStatus("STARTED")
                            .build()
            );
        }

        if (context.managedDependentResourceContext()
                .getWorkflowReconcileResult().orElseThrow()
                .allDependentResourcesReady()) {
            imageBuild.setStatus(
                    new ImageBuildStatusBuilder()
                            .withStatus("READY")
                            .build()
            );
        }

        return UpdateControl.patchStatus(imageBuild);
    }

}

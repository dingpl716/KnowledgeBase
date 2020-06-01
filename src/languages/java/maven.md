## Lifecycle & Phase

Maven有三个标准的Lifecycles, 每个Lifecycle又有若干个预定义的Phase, 每个Phase都可以绑定多个Plugin Goal

  - Lifecycle 1: Clean
  - Lifecycle 2: Default (or Build)
    - Phase 1: **`validate`** -- Validates whether project is correct and all necessary information is available to complete the build process.
    - Phase 2: `initialize` -- Initializes build state, for example set properties.
    - Phase 3: `generate-sources` -- Generate any source code to be included in compilation phase.
    - Phase 4: `process-sources` -- Process the source code, for example, filter any value.
    - Phase 5: `generate-resources` -- Generate resources to be included in the package.
    - Phase 6: `process-resources` -- Copy and process the resources into the destination directory, ready for packaging phase.
    - Phase 7: **`compile**` -- Compile the source code of the project.
    - Phase 8: `process-classes` -- Post-process the generated files from compilation, for example to do bytecode enhancement/optimization on Java classes.
    - Phase 9: `generate-test-sources` -- Generate any test source code to be included in compilation phase.
    - Phase 10: `process-test-sources` -- Process the test source code, for example, filter any values.
    - Phase 11: `generate-test-resources` -- 
    - Phase 12: `process-test-resources` -- 
    - Phase 13: `test-compile` -- Compile the test source code into the test destination directory.
    - Phase 14: `process-test-classes` -- Process the generated files from test code file compilation.
    - Phase 15: **`test`** -- Run tests using a suitable unit testing framework (Junit is one).
    - Phase 16: `prepare-package` -- Perform any operations necessary to prepare a package before the actual packaging.
    - Phase 17: **`package`** -- Take the compiled code and package it in its distributable format, such as a JAR, WAR, or EAR file.
    - Phase 18: `pre-integration-test` -- Perform actions required before integration tests are executed. For example, setting up the required environment.
    - Phase 19: `integration-test` -- Process and deploy the package if necessary into an environment where integration tests can be run.
    - Phase 20: `post-integration-test` -- Perform actions required after integration tests have been executed. For example, cleaning up the environment.
    - Phase 21: **`verify`** -- Run any check-ups to verify the package is valid and meets quality criteria.
    - Phase 22: **`install`** -- Install the package into the local repository, which can be used as a dependency in other projects locally.
    - Phase 23: **`deploy`** -- Copies the final package to the remote repository for sharing with other developers and projects.
  - Lifecycle 3: Site
    - Phase 1: `pre-site`
    - Phase 2: `site`
    - Phase 3: `post-site`
    - Phase 4: `site-deploy`

## Build Profile

Types of Build Profile
Build profiles are majorly of three types.

| Type        | Where it is defined                                                     |
| ----------- | ----------------------------------------------------------------------- |
| Per Project | Defined in the project POM file, pom.xml                                |
| Per User    | Defined in Maven settings xml file (%USER_HOME%/.m2/settings.xml)       |
| Global      | Defined in Maven global settings xml file (%M2_HOME%/conf/settings.xml) |
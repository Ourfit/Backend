package io.ourfit.api.infra.aws;

import io.ourfit.api.infra.aws.config.AwsProperties;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractAwsClient {

  protected final AwsProperties awsProperties;
}

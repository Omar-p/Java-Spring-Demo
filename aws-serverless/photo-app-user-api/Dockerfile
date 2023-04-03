FROM public.ecr.aws/lambda/java:17

COPY target/classes ${LAMBDA_TASK_ROOT}

COPY target/dependency/* ${LAMBDA_TASK_ROOT}/lib/

CMD [ "com.appdeveloperblog.aws.photoapp.users.GetUserHandler::handleRequest" ]
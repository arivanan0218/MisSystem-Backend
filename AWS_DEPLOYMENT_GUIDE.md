# AWS Deployment Guide for MIS System Backend

This guide details the steps to containerize and deploy the MIS System Backend to AWS.

## Part 1: Docker Containerization

### Prerequisites
- Docker and Docker Compose installed locally
- AWS CLI installed and configured
- AWS account with appropriate permissions

### Local Testing with Docker

1. **Build and run the Docker containers locally**:
   ```bash
   docker-compose up --build
   ```

2. **Access the application at**: http://localhost:8081

## Part 2: AWS Deployment

We have multiple options for deploying to AWS. Choose the one that best suits your needs:

### Option 1: AWS Elastic Beanstalk with Docker

1. **Install the EB CLI** (if not already installed):
   ```bash
   pip install awsebcli
   ```

2. **Initialize your Elastic Beanstalk application** (if first time):
   ```bash
   eb init
   ```
   - Select the region where you want to deploy
   - Create a new application or use an existing one
   - Select "Docker" as the platform

3. **Create an environment**:
   ```bash
   eb create mis-backend-env --database --database.engine postgres --database.username postgres --database.password <secure-password>
   ```

4. **Deploy the application**:
   ```bash
   eb deploy
   ```

5. **Get the deployment URL**:
   ```bash
   eb status
   ```

### Option 2: Amazon ECS (Elastic Container Service)

1. **Create an ECR repository**:
   ```bash
   aws ecr create-repository --repository-name mis-backend
   ```

2. **Authenticate Docker to ECR**:
   ```bash
   aws ecr get-login-password --region <region> | docker login --username AWS --password-stdin <account-id>.dkr.ecr.<region>.amazonaws.com
   ```

3. **Tag your Docker image**:
   ```bash
   docker build -t mis-backend .
   docker tag mis-backend:latest <account-id>.dkr.ecr.<region>.amazonaws.com/mis-backend:latest
   ```

4. **Push the image to ECR**:
   ```bash
   docker push <account-id>.dkr.ecr.<region>.amazonaws.com/mis-backend:latest
   ```

5. **Create an ECS cluster** (from AWS Console or CLI)

6. **Define a task definition** with the ECR image

7. **Create a service** within the cluster using the task definition

## Part 3: AWS RDS Configuration

Since the application uses PostgreSQL, you'll need to set up an AWS RDS instance:

1. **Create an RDS PostgreSQL instance** through the AWS Console:
   - Navigate to RDS service
   - Create database
   - Select PostgreSQL engine
   - Configure settings (username, password, instance size)
   - Make sure to place it in the same VPC as your application

2. **Update your application properties** with the RDS endpoint:
   - Create an `application-prod.properties` file with RDS connection details
   - Update the Elastic Beanstalk environment variables to use the RDS connection

### Sample application-prod.properties for RDS

```properties
spring.application.name=mis

server.address=0.0.0.0
server.port=8081

spring.datasource.url=jdbc:postgresql://<your-rds-endpoint>:5432/mis
spring.datasource.username=postgres
spring.datasource.password=<your-secure-password>
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update

# Jwt Configuration
spring.app.jwtSecret=<secure-random-secret-key>
spring.app.jwtExpirationMs=3000000
spring.app.jwtCookieName=mis
```

## Part 4: Important Security Considerations

1. **Never commit sensitive information to version control**:
   - Use environment variables for database credentials
   - Use AWS Parameter Store or Secrets Manager for sensitive data

2. **Configure security groups properly**:
   - Restrict access to the database to only your application
   - Use HTTPS for all communications

3. **Set up CloudWatch monitoring and alarms**:
   - Monitor application logs
   - Set up alerts for unusual activity

## Part 5: CI/CD Pipeline (Optional)

Consider setting up a CI/CD pipeline using AWS CodePipeline:

1. **Create a CodeBuild project** that builds and containerizes the application
2. **Set up CodePipeline** to:
   - Pull code from your repository
   - Build the container image
   - Push to ECR
   - Deploy to ECS or Elastic Beanstalk

## Troubleshooting

- **Check CloudWatch logs** for application errors
- **Verify security group settings** if connection issues occur
- **Check RDS availability** if database connection issues occur

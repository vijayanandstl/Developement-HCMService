# Candidate Microservice

A Spring Boot microservice for managing candidate information, including education, work history, certifications, and skills.

## Features

- Candidate profile management
- Education history tracking
- Work experience management
- Certification tracking
- Skill management
- Event-driven architecture using Kafka
- PostgreSQL database integration
- Docker containerization

## API Endpoints

### Candidate Education
- `POST /api/candidates/{candidateId}/education` - Create education record
- `GET /api/candidates/{candidateId}/education` - Get all education records for a candidate

### Candidate Work History
- `POST /api/candidates/{candidateId}/work-history` - Create work history record
- `GET /api/candidates/{candidateId}/work-history` - Get all work history records for a candidate

### Candidate Certification
- `POST /api/candidates/{candidateId}/certifications` - Create certification record
- `GET /api/candidates/{candidateId}/certifications` - Get all certification records for a candidate

### Skills
- `POST /api/skills` - Create a new skill
- `GET /api/skills` - Get all available skills

### Candidate Skills
- `POST /api/candidates/{candidateId}/skills` - Add a skill to a candidate
- `GET /api/candidates/{candidateId}/skills` - Get all skills for a candidate

## Kafka Topics

- `candidate-created` - When a new candidate is created
- `candidate-updated` - When a candidate is updated
- `candidate-deleted` - When a candidate is deleted
- `candidate-education-created` - When a new education record is created
- `candidate-work-history-created` - When a new work history record is created
- `candidate-certification-created` - When a new certification record is created
- `skill-created` - When a new skill is created
- `candidate-skill-created` - When a skill is added to a candidate

## Build and Run

### Prerequisites
- Java 17 or higher
- Maven
- Docker and Docker Compose
- PostgreSQL
- Kafka

### Building the Application
```bash
mvn clean package
```

### Running with Docker Compose
```bash
docker-compose up -d
```

### Environment Variables
- `SPRING_DATASOURCE_URL` - PostgreSQL connection URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password
- `SPRING_KAFKA_BOOTSTRAP_SERVERS` - Kafka bootstrap servers
- `SERVER_PORT` - Application port (default: 8080)

## Sample Data

The application includes sample data for testing:
- Skills in various categories (Programming, Framework, Database, etc.)
- Sample education records
- Sample work history
- Sample certifications
- Sample candidate skills with proficiency levels

To load the sample data, run:
```bash
psql -U your_username -d your_database -f src/main/resources/sample-data.sql
```

## Database Schema

The application uses the following tables in the `hcm` schema:
- `candidate` - Main candidate information
- `candidate_education` - Education history
- `candidate_work_history` - Work experience
- `candidate_certification` - Certifications
- `skill` - Available skills
- `candidate_skill` - Candidate-skill associations

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License. 
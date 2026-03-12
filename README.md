# RevHire 💼

A full-stack Job Portal application that connects **Job Seekers** and **Employers** on a single platform. Job seekers can browse listings, apply for jobs, build resumes, and track applications — while employers can post jobs, manage listings, and review candidates.

---

## 🚀 Tech Stack

### Frontend
- **Angular** (Standalone Components, Lazy Loading)
- **TypeScript**
- **Angular Router** with route guards (auth, jobseeker, employer)
- **HTTP Interceptors** for JWT token injection

### Backend
- **Java 17+** with **Spring Boot**
- **Spring Security** + **JWT Authentication**
- **Spring Data JPA** / **Hibernate**
- **MySQL** (Database)
- **Lombok**
- **JUnit** (Unit Testing)

---

## ✨ Features

### For Job Seekers
- Register & log in as a Job Seeker
- Browse and search active job listings (by title, location, type, experience)
- View detailed job descriptions
- Apply to jobs
- Track submitted applications
- Save favourite jobs
- Build and manage a resume (skills, education, experience, certifications)
- Receive notifications

### For Employers
- Register & log in as an Employer
- Post new job listings
- Edit or delete existing jobs
- View applications received per job listing
- Manage employer profile

---

## 📁 Project Structure

```
revhire/
├── frontend/                  # Angular application
│   └── src/
│       └── app/
│           ├── components/    # UI components (home, login, dashboards, etc.)
│           ├── services/      # API service layer
│           ├── guards/        # Route guards (auth, jobseeker, employer)
│           ├── interceptors/  # JWT HTTP interceptor
│           └── models/        # TypeScript interfaces
│
└── backend/                   # Spring Boot application
    └── src/main/java/com/revhire/revhire/
        ├── controller/        # REST API controllers
        ├── service/           # Business logic
        ├── entity/            # JPA entities
        ├── repository/        # Spring Data repositories
        └── dto/               # Request/Response DTOs
```

---

## 🔌 API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register/jobseeker` | Register a Job Seeker |
| POST | `/api/auth/register/employer` | Register an Employer |
| POST | `/api/auth/login` | Login (both roles) |

### Jobs
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/jobs/active` | Get all active job listings |
| GET | `/api/jobs/search` | Search jobs (title, location, type, experience) |
| GET | `/api/jobs/:id` | Get job details |
| POST | `/api/jobs` | Create a job (Employer only) |
| PUT | `/api/jobs/:id` | Update a job (Employer only) |
| DELETE | `/api/jobs/:id` | Delete a job (Employer only) |

### Applications
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/applications` | Apply to a job |
| GET | `/api/applications/my` | Get my applications (Job Seeker) |
| GET | `/api/applications/job/:id` | Get applications for a job (Employer) |

### Job Seeker
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/jobseeker/profile` | Get job seeker profile |
| PUT | `/api/jobseeker/profile` | Update profile |
| GET | `/api/jobseeker/saved-jobs` | Get saved jobs |
| POST | `/api/jobseeker/saved-jobs/:jobId` | Save a job |

### Resume
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/resume` | Get resume |
| POST/PUT | `/api/resume` | Create or update resume |

### Notifications
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/notifications` | Get notifications |

---

## ⚙️ Getting Started

### Prerequisites
- **Node.js** (v18+) and **npm**
- **Angular CLI** (`npm install -g @angular/cli`)
- **Java 17+**
- **Maven**
- **MySQL** (running locally on port 3306)

---

### 🗄️ Database Setup

```sql
CREATE DATABASE revhire_db;
```

The schema is auto-managed by Hibernate (`ddl-auto=update`) — no manual migrations needed.

---

### 🖥️ Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Update `src/main/resources/application.properties` with your MySQL credentials:
   ```properties
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. Build and run:
   ```bash
   mvn spring-boot:run
   ```

The backend will start at **http://localhost:8080**

---

### 🌐 Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   ng serve
   ```

The app will be available at **http://localhost:4200**

---

## 🔐 Authentication

RevHire uses **JWT (JSON Web Token)** based authentication.

- On login/register, the server returns a JWT token
- The Angular `AuthInterceptor` automatically attaches the token to all subsequent API requests
- Route guards (`authGuard`, `jobSeekerGuard`, `employerGuard`) protect private routes based on user role

---

## 🧪 Running Tests

### Backend
```bash
cd backend
mvn test
```

---

## 📸 Frontend Routes

| Route | Access | Description |
|-------|--------|-------------|
| `/` | Public | Home page |
| `/login` | Public | Login |
| `/register/jobseeker` | Public | Job Seeker registration |
| `/register/employer` | Public | Employer registration |
| `/jobs` | Public | Browse all jobs |
| `/jobs/:id` | Public | Job detail page |
| `/jobseeker/dashboard` | Job Seeker | Dashboard |
| `/jobseeker/applications` | Job Seeker | My applications |
| `/jobseeker/resume` | Job Seeker | Resume builder |
| `/jobseeker/saved-jobs` | Job Seeker | Saved jobs |
| `/employer/dashboard` | Employer | Employer dashboard |
| `/employer/jobs/create` | Employer | Post a new job |
| `/employer/jobs/:id/edit` | Employer | Edit a job |
| `/employer/jobs/:id/applications` | Employer | View applications |

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).

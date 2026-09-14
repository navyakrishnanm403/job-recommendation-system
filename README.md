# 🎯 Job Recommendation Engine API

A rule-based recommendation API built with **Spring Boot 3 (Java 21)** and **MySQL** that matches candidates to job postings based on skills, experience, location, and salary fit.

---

## 🚀 How to Run the Project

### Prerequisites
- **Java 21**
- **Maven** (or use `./mvnw`)
- **MySQL 8.0+** running locally (or Docker)

---

### Option 1: Run Locally (Maven + Local MySQL)

1. Create a MySQL database named `jobmatch`:
   ```sql
   CREATE DATABASE jobmatch;
   ```

2. Update database credentials in `demo/src/main/resources/application.yml`:
   ```yaml
   spring:
     datasource:
       username: root
       password: your_password
   ```

3. Run Flyway migrations and start the application:
   ```bash
   cd demo
   ./mvnw spring-boot:run
   ```

4. Access **Swagger UI** for interactive API testing:
   👉 **http://localhost:8080/swagger-ui.html**

---

### Option 2: Run via Docker Compose

```bash
cd demo
docker-compose up --build
```
This spins up MySQL 8.0 and the Spring Boot API in isolated containers.

---

## 🧮 Scoring Formula & Weighting Rationale

The overall match score is calculated out of **100 points** across 4 dimensions:

| Dimension | Max Weight | Strategy | Hard Filter? |
|---|---|---|---|
| **Skills** | **50 points** | Must-Have skills gate out; Nice-to-Have skills boost score | ✅ Yes (`MUST_HAVE`) |
| **Experience** | **20 points** | Proportional penalty for missing years | ❌ No (Penalized) |
| **Location** | **15 points** | Exact match > Remote allowed > Mismatch | ❌ No |
| **Salary** | **15 points** | Overlap between candidate expectation and job range | ❌ No |

---

### 1. Skill Score (Max 50 Pts)
* **MUST_HAVE Skills (Hard Filter):** If a candidate lacks even *one* `MUST_HAVE` skill, the job is **excluded from recommendations entirely** (returns `score = null`).
* **NICE_TO_HAVE Skills (Bonus):** If all must-have skills are present, nice-to-have skills earn points proportionally:
  $$\text{Skill Score} = \left( \frac{\text{Nice-to-Have Matched}}{\text{Total Nice-to-Have}} \right) \times 50$$

> **Rationale:** Skills are the single most critical filter in recruitment. Missing a core requirement (e.g. Java for a Senior Java role) makes a candidate unsuitable regardless of salary or location fit.

---

### 2. Experience Score (Max 20 Pts)
* If candidate meets/exceeds `minYearsExperience`: **Full 20 points**.
* If candidate is below requirement:
  $$\text{Exp Score} = \left( \frac{\text{Candidate Years}}{\text{Required Years}} \right) \times 20$$

> **Rationale:** Experience was chosen to **penalize rather than exclude** because a candidate with 3 years applying for a 5-year role may still be a strong fit if their skills and salary match exceptionally well. Excluding them outright discards valuable signal.

---

### 3. Location Score (Max 15 Pts)
* Exact location match: **15 points** (100%)
* Location mismatch, but `remoteAllowed == true`: **10 points** (~67%)
* Location mismatch and `remoteAllowed == false`: **0 points**

> **Rationale:** Remote work is common today. Candidates are often willing to work remotely even if they don't live in the job's physical city.

---

### 4. Salary Score (Max 15 Pts)
* Candidate expects **more than job maximum budget**: **0 points** (Budget mismatch).
* Job minimum meets or exceeds candidate expectation: **15 points** (Comfortable fit).
* Candidate expectation falls within salary range $[Min, Max]$:
  $$\text{Salary Score} = \left( \frac{\text{Max Salary} - \text{Expected Salary}}{\text{Max Salary} - \text{Min Salary}} \right) \times 15$$

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/candidates` | Create candidate profile |
| `GET` | `/candidates/{id}` | Get candidate profile |
| `POST` | `/jobs` | Create job posting |
| `GET` | `/jobs/{id}` | Get job posting |
| `GET` | `/candidates/{id}/recommendations?limit=10` | Top-N job recommendations for candidate |
| `GET` | `/jobs/{id}/recommendations?limit=10` | *(Bonus)* Reverse view: Best-fit candidates for a job |

### Dynamic Weight Customization (Bonus)
You can override default weights directly via query parameters:
```http
GET /candidates/{id}/recommendations?skillWeight=60&experienceWeight=10&locationWeight=15&salaryWeight=15
```

---

## 🧪 Running Unit Tests

Run the test suite covering edge cases (missing must-have skills, experience penalties, salary overlap):

```bash
cd demo
./mvnw test
```

---

## 💡 Assumptions & Future Enhancements

### Assumptions Made:
1. Skill matching is case-insensitive (e.g. `java` matches `Java`).
2. Candidate expected salary is specified as an annual amount matching job salary ranges.

### What I'd Do Differently With More Time:
1. **Synonym / Skill Taxonomy Matching:** Use embedding vectors or alias dictionaries (e.g. `React.js` == `React`).
2. **Pagination:** Add Spring Data `Pageable` support for candidate/job listing endpoints.
3. **Caching:** Cache top recommendation results using Spring Cache / Redis for high-throughput reads.

---

## 🤖 AI Tool Usage

* **AI Assistance Used:** AI was used to generate initial boilerplate code and test scenarios.
* **Overrides / Edits Made:** 
  - Overrode default JPA schema auto-generation in favor of explicit Flyway migration scripts (`V1__create_candidates.sql`, `V2__create_jobs.sql`).
  - Adjusted entity collection strategy to use `@ElementCollection` with `@CollectionTable` instead of redundant `@OneToMany` entities for simple embedded skill objects.

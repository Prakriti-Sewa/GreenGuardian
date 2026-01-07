# 🌍 **GreenGuardian**  
### *A Fully Kotlin-Based Cross-Platform Environmental Issue Reporting Application*

---

## 📌 **Problem Statement**

Environmental issues—such as illegal dumping, plastic pollution, forest damage, water contamination, and improper waste disposal—often go unreported due to **lack of accessible reporting systems**.  
Citizens witness problems but don’t know **where** or **how** to report them.

Governments and NGOs also lack **real-time environmental data**, making problem-solving slower and less efficient.

**GreenGuardian** empowers every citizen to become an environmental protector by providing a simple, unified platform for reporting, mapping, and tracking environmental issues.

---

## 🌱 **Project Overview**

**GreenGuardian** is a **100% Kotlin-based cross-platform application** that lets users:

- 📸 Report environmental issues with images  
- 📍 Automatically attach GPS location  
- 🗺️ View issues on an interactive map  
- 🧭 Track status (submitted → verified → resolved)  
- 📊 Generate analytics for authorities/NGOs  
- 👥 Boost community participation in protecting the planet  

No matter the device — **Android, iOS, Desktop, or Web** — the experience remains the same thanks to Kotlin Multiplatform.

---

## 💥 **Impact on Society**

### ✔️ Rapid environmental response  
Local bodies and NGOs get real-time, structured reports.

### ✔️ Community empowerment  
Every user becomes an environmental guardian.

### ✔️ Cleaner, healthier public spaces  
Faster detection means faster resolution.

### ✔️ Data-driven policy creation  
Environmental heatmaps help stakeholders take action.

### ✔️ Better transparency  
Users can track issue progress and government action.

### ✔️ Encourages a sustainable mindset  
Strengthens public responsibility toward the environment.

---

## 🛠️ **Tech Stack (Only Kotlin)**

### **📱 Cross-Platform Application (Frontend)**
- **Kotlin Multiplatform (KMP)**
- **Compose Multiplatform** (Android, iOS, Desktop, Web)
- **Ktor Client** (networking)
- **SQLDelight** (local storage)
- **Kotlinx Serialization**
- **MapLibre GL for Kotlin** (maps)
- **Koin / Kodein** for DI

### **🌐 Backend (All Kotlin)**
- **Ktor Server**
- **Kotlin Coroutines**
- **Exposed ORM**
- **PostgreSQL** (Issue & User Database)
- **Kotlinx Serialization**
- **Image Storage:**  
  - MinIO (S3-compatible)  
  - or Local Storage (during development)
- **JWT Authentication (Kotlin-only)**

### **💻 Admin Dashboard (Kotlin Web)**
- **Compose Multiplatform for Web**
- **Ktor Client**
- **Kotlinx HTML**

### **☁️ DevOps / Deployment**
- **Docker (Kotlin + JVM images)**
- **NGINX Reverse Proxy**
- **Ktor Engine (Netty / CIO)**
- **GitHub Actions (Kotlin builds)**

---

## 🚀 **Features Roadmap**

- [ ] Capture & report environmental issues  
- [ ] GPS-based auto location tagging  
- [ ] Map view for issues  
- [ ] Issue verification workflow  
- [ ] Admin dashboard  
- [ ] Status tracking for users  
- [ ] Notifications (Firebase + Kotlin)  
- [ ] Analytics dashboard  
- [ ] Dark/Light themes with Compose  

---

## 🤝 **Contributing**

Anyone with Kotlin experience is welcome to contribute.  
Needed help in:
- UI/UX (Compose)
- Kotlin backend (Ktor)
- Database modeling (Exposed)
- Maps integration (MapLibre)
- Documentation & testing

---

## 📜 **License**

MIT License — fully open for modification and distribution.



```mermaid
    flowchart TD

%% =======================
%%  USER APPS
%% =======================

subgraph Users["User Devices (Kotlin Multiplatform)"]
    A1[Android App\nCompose Multiplatform\nKtor Client]
    A2[iOS App\nCompose Multiplatform\nKtor Client]
    A3[Desktop App\nCompose Multiplatform]
    A4[Web App\nCompose Multiplatform Web]
end

%% =======================
%%  API GATEWAY / BACKEND
%% =======================

subgraph API["Backend (Kotlin + Ktor Server)"]
    B1[Ktor Routing\nREST Endpoints]
    B2[Business Logic\nUse Cases]
    B3[Authentication\nJWT]
    B4[Image Handler\nUploads]
end

%% =======================
%%  DATABASE / STORAGE
%% =======================

subgraph Storage["Storage Layer"]
    C1[(PostgreSQL\nExposed ORM)]
    C2[(MinIO / Local Storage\nIssue Images)]
    C3[(Redis\nCaching / Sessions)]
end

%% =======================
%%  ADMIN DASHBOARD
%% =======================

subgraph Admin["Admin Dashboard (Compose Web)"]
    D1[Admin Panel\nView Issues]
    D2[Verification Tools\nApprove/Reject Issues]
    D3[Analytics Dashboard\nKotlin Charts]
end

%% =======================
%%  CONNECTIONS
%% =======================

A1 -->|REST API\nKtor Client| B1
A2 -->|REST API\nKtor Client| B1
A3 -->|REST API| B1
A4 -->|REST API| B1

Admin -->|Ktor Client| B1

B1 --> B2
B2 --> B3

B2 -->|CRUD Ops| C1
B4 -->|Store Images| C2
B2 -->|Cache Reads/Writes| C3

D1 -->|Fetch Data| B1
D2 -->|Update Status| B1
D3 -->|Analytics| B1

```
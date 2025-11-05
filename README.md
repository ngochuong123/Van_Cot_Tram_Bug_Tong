# 🎮 Arkanoid Game (Bài tập lớn OOP)

## 🧩 Giới thiệu

Arkanoid là một trò chơi đập gạch cổ điển được phát triển lại bằng **JavaFX**.  
Dự án này được thực hiện trong khuôn khổ bài tập **Lập trình Hướng Đối Tượng (OOP)**,  
áp dụng các nguyên lý chính như **kế thừa (inheritance)**, **đa hình (polymorphism)**,  
**đóng gói (encapsulation)** và **trừu tượng (abstraction)**.

## 🎯 2. Video Demo

🎥 **Xem video giới thiệu game tại đây:**  
👉 https://youtu.be/your-demo-link

---

📌 *Video demo minh họa gameplay cơ bản của Arkanoid: di chuyển paddle, phá gạch, thu thập power-up và qua màn.*

# UML Diagram
System
<img width="3629" height="512" alt="system" src="https://github.com/user-attachments/assets/c063f1a1-0045-44da-9722-f23411cabb60" />
Core
<img width="2696" height="2402" alt="core" src="https://github.com/user-attachments/assets/f1b187e3-8069-4419-aeb8-d8e9ee71ebfe" />
Model
<img width="19986" height="5146" alt="model" src="https://github.com/user-attachments/assets/00c1b949-7eb1-40b4-b373-a3921410bdd3" />
UI
<img width="5757" height="1100" alt="ui" src="https://github.com/user-attachments/assets/53aa1ec3-8a3b-40d8-bcdc-e1d4941b082e" />
Âm thanh
<img width="2457" height="806" alt="Audio" src="https://github.com/user-attachments/assets/45a4a3d5-a17c-486e-b74e-940f7dbad215" />
App - nơi chay chương trình
<img width="2457" height="806" alt="Audio" src="https://github.com/user-attachments/assets/fc3e6ded-e138-48a2-99e4-62666dee257f" />
## 👥 Thành viên nhóm

| Họ và Tên           | Chức vụ             | Đảm nhiệm chính                       | Mức độ đóng góp |
|----------------------|--------------------|----------------------------------------|-----------------|
| Ngô Xuân Hậu         | Trưởng nhóm        |  |  |
| Nguyễn Huy Hoàng     | Thành viên         |  |  |
| Nguyễn Quang Hồng    | Thành viên         | |  |
| Nguyễn Ngọc Hưởng    | Thành viên         | Xử lý giao diện ui - chuyển cảnh (menu, pause, gameover,..),sơ đồ lớp, tham gia 1 phần vào logic chính (tính điểm, mất mạng,..)  |  |

## 🕹️ Cách chơi

🎯 **Mục tiêu:**  
Giữ bóng không rơi khỏi màn hình và phá vỡ toàn bộ các khối gạch để qua màn chơi.

---

### 🧭 **Điều khiển**
- ⬅️ **Phím Trái** / **Phím A**: Di chuyển paddle sang trái  
- ➡️ **Phím Phải** / **Phím D**: Di chuyển paddle sang phải  
- 🔸 **Phím Space**: Thả bóng để bắt đầu trò chơi

---

### ⚙️ **Luật chơi**
- Di chuyển paddle để giữ bóng không rơi khỏi màn hình.  
- Phá vỡ toàn bộ các viên gạch để vượt qua màn chơi.  
- Khi bóng rơi ra ngoài, bạn sẽ **mất một mạng**.  
- Trò chơi **kết thúc (Game Over)** khi bạn hết mạng,  
  hoặc **chiến thắng** khi phá vỡ toàn bộ gạch.

---

### 💫 **Power-Ups (Hiệu ứng hỗ trợ)**
Trong quá trình chơi, hãy thu thập các **Power-Up** để nhận hiệu ứng đặc biệt:

| Power-Up | Hiệu ứng |
|-----------|----------|
| <img width="32" height="32" alt="expand_paddle" src="https://github.com/user-attachments/assets/40f8909b-beee-4858-ba7f-422b0bb9aa06" /> Tăng kích thước paddle | Paddle dài hơn, dễ đỡ bóng hơn |
| <img width="32" height="32" alt="expand_paddle" src="https://github.com/user-attachments/assets/40f8909b-beee-4858-ba7f-422b0bb9aa06" /> Giảm kích thước paddle | Paddle ngắn lại, tăng độ khó |
| <img width="32" height="32" alt="BigBall" src="https://github.com/user-attachments/assets/e7b77fe6-09b4-4050-bd7e-991ac0c358ce" /> Tăng kích thước bóng | Bóng to hơn, dễ chạm vào gạch |
| <img width="32" height="32" alt="ShieldPowerUp" src="https://github.com/user-attachments/assets/1feb0f21-7283-48be-82f8-5eca3e2f8835" /> Khiên chắn bóng | Đỡ được 1 lần bóng rơi khỏi màn hình |
| <img width="32" height="32" alt="FireBallPowerUp" src="https://github.com/user-attachments/assets/71caf6f7-9a49-4672-bc0c-d7f5a7502209" /> Bóng lửa | Phá được mọi loại gạch chỉ với 1 chạm |
| <img width="32" height="32" alt="x3_Ball" src="https://github.com/user-attachments/assets/a34b747e-c9a8-4ce6-ad04-b8bed707f13f" /> Nhân bóng | Tạo thêm 3 quả bóng mới trên sân |


---

💔 **Lưu ý:** Mỗi lần bóng rơi hết khỏi màn hình, bạn **mất 1 mạng**.  
🏆 Phá hết gạch để **chiến thắng trò chơi!**


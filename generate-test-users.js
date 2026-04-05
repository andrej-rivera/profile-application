const fs = require('fs');

// Generate test users
const users = [];
for (let i = 1; i <= 100; i++) {
    users.push({
        username: `user${i}`,
        password: `pass`,
        email: `testuser${i}@example.com`,
        roles: "USER"
    });
}

// Write to file
fs.writeFileSync('test-users.json', JSON.stringify(users, null, 2));
console.log('Generated 100 test users in test-users.json');
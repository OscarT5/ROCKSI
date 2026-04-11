// tests/login.spec.js
const { test, expect } = require('@playwright/test');

test('abrir pagina', async ({ page }) => {
    await page.goto('http://localhost:8080');
    await expect(page).toHaveTitle(/ROCKSI/);
});
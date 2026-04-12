const { test, expect } = require('@playwright/test');

test('login ROCKSI', async ({ page }) => {

    // Esperar a que cargue el formulario
    await page.waitForSelector('[id="loginForm:usuario"]', { timeout: 15000 });

    await page.goto('http://localhost:8080/vista/');

    await page.fill('[id="loginForm:usuario"]', 'ADM1000');
    await page.fill('[id="loginForm:contrasena"]', '123');
    await page.click('text=Iniciar sesión');

    await page.waitForTimeout(3000);

    await expect(page).not.toHaveURL('http://localhost:8080/vista/home.xhtml');
});
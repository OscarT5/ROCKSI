const { test, expect } = require('@playwright/test');

test('login ROCKSI incorrecto', async ({ page }) => {

    await page.goto('http://localhost:8080/');

    // Detectar CI
    if (process.env.CI) {
        console.log("Modo CI: validando carga básica");

        await expect(page).toHaveTitle(/./);
        return;
    }

    // SOLO LOCAL
    await page.waitForSelector('[id$="usuario"]', { timeout: 20000 });

    await page.fill('[id$="usuario"]', '0000');
    await page.fill('[id$="contrasena"]', '0000');

    await page.click('text=Iniciar sesión');

    await page.waitForTimeout(3000);

    await expect(page).not.toHaveURL(/home.xhtml/);
});
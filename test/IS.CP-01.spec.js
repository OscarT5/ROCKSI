const { test, expect } = require('@playwright/test');

test('login ROCKSI', async ({ page }) => {

    await page.goto('http://localhost:8080/');

    if (process.env.CI) {
        console.log("Modo CI: validando carga básica");

        // Solo validar que cargó algo
        await expect(page).toHaveTitle(/./);

        return;
    }

    // Local e BD
    await page.waitForSelector('[id$="usuario"]', { timeout: 20000 });

    await page.fill('[id$="usuario"]', 'ADM1000');
    await page.fill('[id$="contrasena"]', '1234');

    await page.click('text=Iniciar sesión');

    await page.waitForURL('**/home.xhtml', { timeout: 20000 });

    await expect(page).toHaveURL(/home.xhtml/);
});
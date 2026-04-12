const { test, expect } = require('@playwright/test');

test('login ROCKSI correcto', async ({ page }) => {

    await page.goto('http://localhost:8080/vista/', {
        waitUntil: 'networkidle'
    });

    // Esperar a que el input realmente exista (JSF)
    await page.waitForSelector('[id$="usuario"]', { timeout: 20000 });

    // Llenar formulario
    await page.fill('[id$="usuario"]', 'ADM1000');
    await page.fill('[id$="contrasena"]', '123');

    // Click
    await page.click('text=Iniciar sesión');

    // Validar que redirige al home
    await page.waitForURL('**/home.xhtml', { timeout: 20000 });

    await expect(page).toHaveURL(/home.xhtml/);
});
const { test, expect } = require('@playwright/test');

const BASE_URL = 'http://localhost:8080';

async function login(page) {
    await page.goto(BASE_URL);

    await page.waitForSelector('[id$="usuario"]', { timeout: 30000 });

    await page.fill('[id$="usuario"]', 'ADM1000');
    await page.fill('[id$="contrasena"]', '123');

    await page.click('input[value="Iniciar sesión"]');

    await page.waitForURL('**/home.xhtml', { timeout: 20000 });
}

test('Alta cliente sin sexo (fallido)', async ({ page }) => {

    await login(page);

    await page.goto(`${BASE_URL}/vista/clientes.xhtml`);

    await page.click('button:has-text("Registrar Cliente")');

    await page.waitForSelector('[id$="nombreAlta"]');

    await page.fill('[id$="nombreAlta"]', 'Test');
    await page.fill('[id$="apellidoAlta"]', 'Usuario');
    await page.fill('[id$="telefonoAlta"]', '1234567890');

    await page.click('[id$="registrarBtn"]');

    const error = page.locator('.ui-messages-error');

    await expect(error).toBeVisible({ timeout: 10000 });
    await expect(error).toContainText(/sexo/i);
});
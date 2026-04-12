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

test('Alta cliente correcta', async ({ page }) => {

    await login(page);

    await page.goto(`${BASE_URL}/vista/clientes.xhtml`);

    await page.click('button:has-text("Registrar Cliente")');

    await page.waitForSelector('[id$="nombreAlta"]', { timeout: 20000 });

    await page.fill('[id$="nombreAlta"]', 'Juan');
    await page.fill('[id$="apellidoAlta"]', 'Perez');
    await page.fill('[id$="telefonoAlta"]', '1234567890');
    await page.fill('[id$="segundoTelefonoAlta"]', '0987654321');

    await page.click('[id$="sexoAlta_label"]');
    await page.click('li:has-text("Masculino")');

    await page.click('[id$="registrarBtn"]');

    await page.waitForURL('**/pagos.xhtml', { timeout: 20000 });

    await expect(page).toHaveURL(/pagos.xhtml/);
});
// test/AC.US-01.spec.js
// Prueba UI — Alta de Recepcionista
// Sistema: Rocksi | Autor: Erick

const { test, expect } = require('@playwright/test');

// ── Datos de prueba ──────────────────────────────────────────────────────────
const BASE_URL    = 'http://localhost:8080';
const USUARIO     = 'ADM1000';
const PASSWORD    = '123';

const NOMBRE_TEST = 'Test Automatizado';
const CORREO_TEST = 'test.auto@rocksi.com';
const PASS_TEST   = 'Test1234!';

// ── Helper: login ────────────────────────────────────────────────────────────
async function login(page) {
    await page.goto(`${BASE_URL}/login.xhtml`);
    await page.waitForLoadState('networkidle');

    await page.locator('input[id$="usuario"]').fill(USUARIO);
    await page.locator('input[id$="contrasena"]').fill(PASSWORD);
    await page.locator('button:has-text("Iniciar sesión"), input[value="Iniciar sesión"]').click();
    await page.waitForURL(`**home.xhtml**`, { timeout: 15000 });
}

// ── TEST: Alta de Recepcionista ───────────────────────────────────────────────
test('Alta de recepcionista exitosa', async ({ page }) => {

    // 1. Login como admin
    await login(page);

    // 2. Navegar a Usuarios
    await page.goto(`${BASE_URL}/usuarios.xhtml`);
    await page.waitForLoadState('networkidle');

    // 3. Clic en "Registrar Usuario"
    await page.locator('button:has-text("Registrar Usuario")').click();

    // 4. Esperar que el diálogo sea visible
    const dialogo = page.locator('.ui-dialog:has-text("Nuevo Recepcionista")');
    await expect(dialogo).toBeVisible({ timeout: 6000 });

    // 5. Llenar el formulario
    await dialogo.locator('input[placeholder="Nombre"]').fill(NOMBRE_TEST);
    await dialogo.locator('input[id$="correoRecep"]').fill(CORREO_TEST);
    await dialogo.locator('input[type="password"]').first().fill(PASS_TEST);

    // 6. Clic en Registrar
    await dialogo.locator('button:has-text("Registrar")').click();

    // 7. Verificar mensaje de éxito en el growl
    const growl = page.locator('.ui-growl-item');
    await expect(growl).toBeVisible({ timeout: 8000 });
    await expect(growl).not.toContainText('error', { ignoreCase: true });

    // 8. Esperar que la tabla se actualice via AJAX
    await page.waitForTimeout(2000);

    // 9. Verificar que el nombre aparece en la tabla actualizada
    await expect(page.getByText(NOMBRE_TEST).first()).toBeVisible({ timeout: 8000 });
});

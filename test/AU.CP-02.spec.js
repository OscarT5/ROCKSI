// test/AC.US-02.spec.js
// Prueba UI — Baja (Eliminación) de Recepcionista
// Sistema: Rocksi | Autor: Erick

const { test, expect } = require('@playwright/test');

// ── Datos de prueba ──────────────────────────────────────────────────────────
const BASE_URL   = 'http://localhost:8080';
const USUARIO    = 'ADM1000';
const PASSWORD   = '123';
const ID_BAJA    = 'UR1003'; // Cambia al ID que exista en tu BD

// ── Helper: login ────────────────────────────────────────────────────────────
async function login(page) {
    await page.goto(`${BASE_URL}/login.xhtml`);
    await page.waitForLoadState('networkidle');

    await page.locator('input[id$="usuario"]').fill(USUARIO);
    await page.locator('input[id$="contrasena"]').fill(PASSWORD);
    await page.locator('button:has-text("Iniciar sesión"), input[value="Iniciar sesión"]').click();
    await page.waitForURL(`**home.xhtml**`, { timeout: 15001 });
}

// ── TEST: Baja de Recepcionista ───────────────────────────────────────────────
test('Baja de recepcionista exitosa', async ({ page }) => {

    // 1. Login como admin
    await login(page);

    // 2. Navegar a Usuarios
    await page.goto(`${BASE_URL}/usuarios.xhtml`);
    await page.waitForLoadState('networkidle');

    // 3. Verificar que el usuario a eliminar SÍ existe antes de borrar
    await expect(page.getByText(ID_BAJA).first()).toBeVisible({ timeout: 5000 });

    // 4. Clic en "Baja Usuario"
    await page.locator('button:has-text("Baja Usuario")').click();

    // 5. Esperar el diálogo "Eliminar Recepcionista"
    const dialogoBaja = page.locator('.ui-dialog:has-text("Eliminar Recepcionista")');
    await expect(dialogoBaja).toBeVisible({ timeout: 6000 });

    // 6. Ingresar el ID del recepcionista a eliminar
    await dialogoBaja.locator('input[placeholder="Ej. UR1001"]').fill(ID_BAJA);

    // 7. Clic en "Eliminar" — abre el diálogo de confirmación
    await dialogoBaja.locator('button:has-text("Eliminar")').click();

    // 8. Esperar el diálogo de confirmación del recepcionista
    const dialogoConfirm = page.locator('.ui-dialog')
        .filter({ hasText: '¿Seguro que deseas eliminar este usuario?' });
    await expect(dialogoConfirm).toBeVisible({ timeout: 6000 });

    // 9. Confirmar eliminación
    await dialogoConfirm.locator('button:has-text("Sí, Eliminar")').click();

    // 10. El diálogo de confirmación debe cerrarse
    await expect(dialogoConfirm).not.toBeVisible({ timeout: 8000 });

    // 11. Verificar mensaje de éxito en el growl
    const growl = page.locator('.ui-growl-item');
    await expect(growl).toBeVisible({ timeout: 8000 });
    await expect(growl).not.toContainText('error', { ignoreCase: true });

    // 12. Clic en "Actualizar Tabla" para refrescar via AJAX
    await page.waitForTimeout(1000);
    await page.locator('button:has-text("Actualizar Tabla")').first().click();
    await page.waitForTimeout(2000);

    // 13. Verificar que el ID eliminado ya NO aparece en la tabla de recepcionistas
    const tablaRecep = page.locator('.ui-datatable').first();
    await expect(tablaRecep).not.toContainText(ID_BAJA, { timeout: 8000 });
});

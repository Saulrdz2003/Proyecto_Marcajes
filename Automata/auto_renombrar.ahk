; === Script: auto_renombrar.ahk ===
#NoEnv
SendMode Input
SetWorkingDir %A_ScriptDir%
FileEncoding, UTF-8

; === Ruta de destino ===
rutaDestino := A_Desktop "\Marcajes"

; Crear la carpeta si no existe
IfNotExist, %rutaDestino%
    FileCreateDir, %rutaDestino%

; Variables para el loop
ultimoArchivo := ""
ultimaFecha := 0

; === Buscar el archivo más reciente con extensiones csv, xls o xlsx ===
for _, ext in ["csv", "xls", "xlsx"] {
    Loop, Files, %rutaDestino%\*.%ext%, F
    {
        if (A_LoopFileTimeModified > ultimaFecha) {
            ultimaFecha := A_LoopFileTimeModified
            ultimoArchivo := A_LoopFileFullPath
        }
    }
}

; === Si se encontró al menos uno, renombrar el más reciente ===
if (ultimoArchivo != "")
{
    FormatTime, fechaActual,, yyyy-MM-dd
    SplitPath, ultimoArchivo, nombre, dir, ext, nombreSinExt

    ; Nuevo nombre SIEMPRE con extensión .csv
    nuevoNombre := dir "\" fechaActual ".csv"

    ; Cambiar nombre (sobrescribe si ya existe)
    FileMove, %ultimoArchivo%, %nuevoNombre%, 1
}
ExitApp

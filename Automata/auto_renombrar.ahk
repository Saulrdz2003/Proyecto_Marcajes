; === Script: auto_renombrar.ahk ===
#NoEnv
SendMode Input
SetWorkingDir %A_ScriptDir%
FileEncoding, UTF-8

; === Ruta de destino ===
; Carpeta "Marcajes" en el escritorio del usuario
rutaDestino := A_Desktop "\Marcajes"

; Variables para el loop
fileCount := 0
ultimoArchivo := ""

; Crear la carpeta si no existe
IfNotExist, %rutaDestino%
    FileCreateDir, %rutaDestino%

; Buscar archivos en la carpeta
Loop, Files, %rutaDestino%\*, F
{
    fileCount++
    ultimoArchivo := A_LoopFileFullPath
}

; Si hay exactamente un archivo, renombrarlo silenciosamente
if (fileCount = 1)
{
    FormatTime, fechaActual,, yyyy-MM-dd
    SplitPath, ultimoArchivo, nombre, dir, ext, nombreSinExt

    ; Nuevo nombre CSV
    nuevoNombre := dir "\" fechaActual ".csv"

    ; Cambiar nombre sin preguntar
    FileMove, %ultimoArchivo%, %nuevoNombre%, 1
}
ExitApp

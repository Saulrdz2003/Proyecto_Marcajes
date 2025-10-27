#NoEnv  
SendMode Input  
SetWorkingDir %A_ScriptDir%  

CarpetaDrive := "G:\Mi unidad\Cartas"  
if !FileExist(CarpetaDrive)  
    FileCreateDir, %CarpetaDrive%  

CarpetaOrigen := A_Desktop "\archivos"  
Loop, Files, %CarpetaOrigen%\*.pdf  
{  
    FileCopy, %A_LoopFileFullPath%, %CarpetaDrive%\%A_LoopFileName%, 1  
}  

ExitApp

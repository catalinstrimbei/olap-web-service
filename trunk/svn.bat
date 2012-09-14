@ECHO OFF
SET PATH=%PATH%;D:\Program Files\Visual Paradigm for UML 10.0\bin\vp_windows\svn\bin
FOR %%A IN (%*) DO (	
	if %%A==lock (
		echo found %%A
		goto do_nothing
	)
)
rem echo do: svn %*
svn %*
goto end

:do_nothing
echo do_nothing

:end
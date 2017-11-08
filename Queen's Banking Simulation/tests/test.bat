::Batch file for testing the input files and outputing it into text and logs
@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

:: Create an array for the list of actions
set list=createacct deleteacct deposit login transfer withdraw

:: go to the start of the test_%%i_
goto :runtest 

:runtest
	cd input
	::for each action folder in the input folder, enter the action folder
	for %%i in (%list%) do (
	
		cd %%i
		
		echo Running Test: %%i
		
		::set variables
		set count=1
		set filename=%%i
		
		::for each file in the folder
		for %%k in (*) do (
			
			cd ..\..
			::run the test piping the input from input\action\textfile and outputing the log to output\action\log
			java -jar qbasic.jar validaccounts.txt transactionsummaryfile.txt < input\%%i\test_%%i_!count!.txt >output\%%i\log\!filename!_!count!.log
			
			::rename transactionsummaryfile to expected action output and move it to the corresponding output folder, output\action\tsf
			ren transactionsummaryfile.txt test_%%i_!count!.txt
			move test_%%i_!count!.txt output\%%i\tsf\%%k
			
			::back to input action folder
			cd input\%%i
			
			::increase the counter
			set	/A count=count+1
		)
		::once done, show that it was a success
		echo --------
		echo Success!
		echo --------
		cd ..
	)
	cd ..
	
	::move to next function
	goto :validate
	
:validate
	cd output
	::for each action in output, go to the transaction summary folder of the action folder
	for %%i in (%list%) do (
	
		cd %%i\tsf
		
		echo Validating outputs: %%i
		
		::set variables
		set count=1
		set filename=%%i
		
		::for each textfile in folder
		for %%k in (*.txt) do (
			cd ..\..\..
			::compare the test output to expected output for the transactionsummaryfile and the log
			fc /c output\%%i\tsf\test_!filename!_!count!.txt expected\%%i\expected_summary_!count!.txt
			echo -----------------------------------------------------------------------------------
			fc /c output\%%i\log\!filename!_!count!.log expected\%%i\expected_output_!count!.log
			echo -----------------------------------------------------------------------------------
			
			::back to output\action\tsf
			cd output\%%i\tsf
			
			::increase counter
			set	/A count=count+1
		)
		cd ..\..
	)
	::pause to show the final results
	pause
	cd ..
	
	::go to the end of file when complete
	goto :eof
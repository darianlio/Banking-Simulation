::Batch file for testing the input files and outputing it into text and logs
@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

set list=day1 day2 day3 day4 day5

:: Create an array for the list of actions
goto :runWeekly 

:runWeekly	
	
	:: delete original master accounts file and valid accounts file to restart
	del masteraccountsfile.txt
	del validaccounts.txt
	
	:: create new master accounts file and valid accounts file
	echo|set /p="" >> masteraccountsfile.txt
	echo|set /p="" >> validaccounts.txt
	
	::for each action folder in the input folder, enter the action folder
	for %%i in (%list%) do (
		
		cd input\%%i
		
		echo Running Test: %%i
		
		::set variables
		set count=1
		
		::for each file in the folder
		for %%k in (*) do (
			echo Running Test: %%k
			cd ..\..
			::run the test piping the input from input\day\textfile
			java -jar qfront.jar validaccounts.txt transactionsummaryfile.txt < input\%%i\%%i_!count!.txt
			
			::rename tsf file to tsf_day(i)
			ren transactionsummaryfile.txt tsf_%%i_!count!.txt
			move tsf_%%i_!count!.txt output\%%i
			
			::back to input action folder
			cd input\%%i
			
			::increase the counter
			set	/A count+=1
		)
		cd ..\..
		cd output\%%i
		
		:: Delete last line of each TSM for the day and remakes each one into a new file without the last line 'EOS'
		findstr /V "EOS" tsf_%%i_1.txt > tsf_%%i_new_1.txt
		findstr /V "EOS" tsf_%%i_2.txt > tsf_%%i_new_2.txt
		findstr /V "EOS" tsf_%%i_3.txt > tsf_%%i_new_3.txt
		
		:: Concatenate each file into merged transaction summary and move to main folder
		copy tsf_%%i_new_1.txt + tsf_%%i_new_2.txt + tsf_%%i_new_3.txt mergedtsf.txt
		:: Add EOS at end of file
		echo|set /p="EOS" >> mergedtsf.txt
		
		move mergedtsf.txt ..\..

		:: Go back to main folder
		cd ..\..
		
		:: Run back office with merged tsf as input
		java -jar qback.jar masteraccountsfile.txt validaccounts.txt < mergedtsf.txt
		
		::once done, show that it was a success
		echo ----------------------
		echo Completed %%i
		echo ----------------------
		pause
	)
	cd ..
	
	::move to next function
	goto :eof
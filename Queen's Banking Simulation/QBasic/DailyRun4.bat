::Batch file for testing the input files and outputing it into text and logs
@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

:: Create an array for the list of actions
goto :runDaily 

:runDaily	
	::for each action folder in the input folder, enter the action folder
	cd input\day4
	echo Running Test: day4
	
	::set variables
	set count=1
	
	::for each file in the folder
	for %%k in (*) do (
		echo Running Test: %%k
		cd ..\..
		::run the test piping the input from input\day\textfile
		java -jar qfront.jar validaccounts.txt transactionsummaryfile.txt < input\day4\day4_!count!.txt
		
		::rename tsf file to tsf_day(i)
		ren transactionsummaryfile.txt tsf_day4_!count!.txt
		move tsf_day4_!count!.txt output\day4
		::back to input action folder
		cd input\day4
		
		::increase the counter
		set	/A count+=1
	)
	cd ..\..
	cd output\day4
	
	:: Delete last line of each TSF for the day and remakes each one into a new file without the last line 'EOS'
	findstr /V "EOS" tsf_day4_1.txt > tsf_day4_new_1.txt
	findstr /V "EOS" tsf_day4_2.txt > tsf_day4_new_2.txt
	findstr /V "EOS" tsf_day4_3.txt > tsf_day4_new_3.txt
		
	:: Merge each file into merged transaction summary and move to main folder
	copy tsf_day4_new_1.txt + tsf_day4_new_2.txt + tsf_day4_new_3.txt mergedtsf4.txt
	:: Add EOS at end of file
	echo|set /p="EOS" >> mergedtsf4.txt
		
	move mergedtsf4.txt ..\..

	:: Go back to main folder
	cd ..\..
		
	:: Run back office with merged tsf as input
	java -jar qback.jar masteraccountsfile.txt validaccounts.txt < mergedtsf4.txt
		
	::once done, show that it was a success
	echo ----------------------
	echo Completed Day 4
	echo ----------------------
	pause
)
cd ..
	
::move to next function
goto :eof
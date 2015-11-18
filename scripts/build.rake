namespace :android do

  desc 'debug'
  task:debug do
    sh './gradlew installDebug'
    sh 'adb shell am start -D -n  ian_ellis.ankomvvm/.presentation.MainActivity'
  end

end
# MobilERP

**_Abandoned in favor of [MobelERP Kotlin][7]_**

A small self-hosted ERP that works with your smarphone

[![Build Status](https://travis-ci.org/eligiobz/mobilerp-android.svg?branch=master)][1]

## The idea

Have a small business who want or need an ERP but have no internet access or can't afford to pay a susbcrition to a cloud hosted service? This solution is for you.
You can either find someone to run this on Raspberry Pi and help you configure your Android phone to work with it or DIY.

This is MobilERP-android. Check the companion repo MobilERP-server [MobilERP-server][2]

## What it currently does

It currently fetch some reports (See [MobilERP-server Roadmap][6]). It also scans a product's barcode, inserts it into the database or updates it quantity and/or price.

## Roadmap

- [ ] Code refactoring
- [ ] Better UI
	- [ ] Implement navbar
	- [ ] Split daily activities from management activities
	- [ ] Revise font text and position
- [ ] Implement service discovery

## Licenses

This project is uses the ZXing Android Embedded project by [journeyapp][4], released under the Apache 2.0 license. See below.

Licensed under the [Apache License 2.0][5]

	Copyright (C) 2012-2017 ZXing authors, Journey Mobile
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

[1]: https://travis-ci.org/eligiobz/mobilerp-android
[2]: https://github.com/eligiobz/mobilerp-server
[3]: https://github.com/eligiobz/mobilerp-server
[4]: https://github.com/journeyapps/zxing-android-embedded
[5]: http://www.apache.org/licenses/LICENSE-2.0
[6]: https://github.com/eligiobz/mobilerp-android#Roadmap
[7]: https://github.com/eligiobz/mobilerp-kotlin

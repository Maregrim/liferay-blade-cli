/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.liferay.blade.cli;

import aQute.bnd.header.Attrs;
import aQute.bnd.osgi.Domain;

import com.liferay.blade.cli.gradle.GradleExec;
import com.liferay.blade.cli.gradle.GradleTooling;

import java.io.File;
import java.io.IOException;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.easymock.EasyMock;
import org.easymock.IExpectationSetters;

import org.powermock.api.easymock.PowerMock;

/**
 * @author Christopher Bryan Boyd
 */
public class MockUtil {

	public static void stubDomain(boolean returnBsn, boolean returnFragment) throws IOException {
		PowerMock.mockStatic(Domain.class);

		EasyMock.expect(
			Domain.domain(EasyMock.isA(File.class))
		).andStubAnswer(
			() -> {
				Domain domain = EasyMock.createNiceMock(Domain.class);

				if (returnBsn || returnFragment) {
					Entry<String, Attrs> mock = new AbstractMap.SimpleEntry<>(null, null);

					if (returnBsn) {
						EasyMock.expect(
							domain.getBundleSymbolicName()
						).andStubReturn(
							mock
						);
					}
					else {
						EasyMock.expect(
							domain.getBundleSymbolicName()
						).andStubReturn(
							null
						);
					}

					if (returnFragment) {
						EasyMock.expect(
							domain.getFragmentHost()
						).andStubReturn(
							mock
						);
					}
					else {
						EasyMock.expect(
							domain.getFragmentHost()
						).andStubReturn(
							null
						);
					}
				}

				EasyMock.replay(domain);

				return domain;
			}
		);

		PowerMock.replay(Domain.class);
	}

	public static void stubGradleExec() throws Exception {
		GradleExec gradle = EasyMock.createNiceMock(GradleExec.class);

		EasyMock.expect(
			gradle.executeGradleCommand(EasyMock.anyString())
		).andStubReturn(
			0
		);

		EasyMock.replay(gradle);

		PowerMock.mockStatic(GradleExec.class);

		IExpectationSetters<GradleExec> newGradleExec = PowerMock.expectNew(
			GradleExec.class, EasyMock.isA(BladeNoFail.class));

		newGradleExec.andStubReturn(gradle);

		PowerMock.replay(GradleExec.class);
	}

	public static void stubGradleTooling(File returnFile) throws Exception {
		PowerMock.mockStatic(GradleTooling.class);

		IExpectationSetters<Set<File>> outputFiles = EasyMock.expect(
			GradleTooling.getOutputFiles(EasyMock.isA(File.class), EasyMock.isA(File.class)));

		outputFiles.andStubReturn(new HashSet<>(Arrays.asList(returnFile)));

		PowerMock.replay(GradleTooling.class);
	}

	public static void stubUtil() {
		PowerMock.mockStaticPartialNice(Util.class, "canConnect", "getBuiltinCommands", "getExtensions");

		IExpectationSetters<Boolean> canConnect = EasyMock.expect(
			Util.canConnect(EasyMock.anyString(), EasyMock.anyInt()));

		canConnect.andStubReturn(true);

		Map<BaseArgs, BaseCommand<?>> map = new HashMap<>();
		map.put(new DeployCommandArgs(), new DeployCommand());

		EasyMock.expect(Util.getBuiltinCommands()).andStubReturn(map);

		Map<BaseArgs, BaseCommand<?>> eMap = new HashMap<>();

		EasyMock.expect(Util.getExtensions()).andStubReturn(eMap);

		PowerMock.replay(Util.class);
	}

}